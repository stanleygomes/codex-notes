#!/usr/bin/env node
import { spawn } from 'node:child_process';
import { dirname, join } from 'node:path';
import { fileURLToPath } from 'node:url';

const root = join(dirname(fileURLToPath(import.meta.url)), '..');
const vscodeTestBin = join(root, 'node_modules', '.bin', 'vscode-test');

const ansiPattern = new RegExp(`${String.fromCharCode(27)}\\[[0-9;]*m`, 'g');
const stripAnsi = (value) => value.replace(ansiPattern, '');

const noisePatterns = [
  /No signed-in session resolved for resource/i,
  /StorageMainService:/i,
  /\[shared storage\]/i,
  /update#setState/i,
  /update#ctor/i,
  /RemoteAgentHost\]/i,
  /\[AgentHost/i,
  /AgentHostProcessManager:/i,
  /AgentHost:renderer/i,
  /ChatModelSelection\]/i,
  /AccountPolicyGate\]/i,
  /Settings Sync:/i,
  /'cached-data' is not in the list of known options/i,
  /Extension host with pid \d+ exited with code: 0, signal: unknown/i,
  /\[DEP0169\] DeprecationWarning/i,
  /Use `Code Helper --trace-deprecation/i,
  /\(Use `Code Helper --trace-deprecation/i,
  /Reconciling: desired=\[\]/i,
  /Acquiring MessagePort to agent host/i,
  /MessagePort acquired, creating client/i,
  /Protocol connection established/i,
];

const isNoise = (line) => {
  const plain = stripAnsi(line).trim();
  if (plain.length === 0) {
    return true;
  }
  return noisePatterns.some((pattern) => pattern.test(plain));
};

const writeFiltered = (chunk, stream) => {
  const text = chunk.toString();
  const endsWithNewline = text.endsWith('\n') || text.endsWith('\r');
  const lines = text.split(/\r?\n/);
  const last = endsWithNewline ? '' : (lines.pop() ?? '');

  for (const line of lines) {
    if (isNoise(line)) {
      continue;
    }
    stream.write(`${line}\n`);
  }

  return last;
};

let stdoutCarry = '';
let stderrCarry = '';

const child = spawn(vscodeTestBin, process.argv.slice(2), {
  cwd: root,
  env: process.env,
  stdio: ['inherit', 'pipe', 'pipe'],
});

child.stdout.on('data', (chunk) => {
  stdoutCarry = writeFiltered(stdoutCarry + chunk.toString(), process.stdout);
});

child.stderr.on('data', (chunk) => {
  stderrCarry = writeFiltered(stderrCarry + chunk.toString(), process.stderr);
});

child.on('error', (error) => {
  console.error(error);
  process.exit(1);
});

child.on('close', (code, signal) => {
  if (stdoutCarry && !isNoise(stdoutCarry)) {
    process.stdout.write(stdoutCarry);
  }
  if (stderrCarry && !isNoise(stderrCarry)) {
    process.stderr.write(stderrCarry);
  }

  if (signal) {
    process.kill(process.pid, signal);
    return;
  }

  process.exit(code ?? 1);
});
