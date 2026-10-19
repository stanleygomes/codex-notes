import { fileURLToPath } from 'node:url';
import { defineConfig } from 'vitest/config';

export default defineConfig({
  resolve: {
    extensions: ['.ts', '.js', '.json'],
    alias: {
      vscode: fileURLToPath(
        new URL('./src/test/unit/mocks/vscode.ts', import.meta.url),
      ),
    },
  },
  test: {
    name: 'unit',
    include: ['src/**/*.test.ts'],
    exclude: ['src/test/e2e/**', '**/node_modules/**'],
    environment: 'node',
    globals: false,
    mockReset: true,
    setupFiles: ['./src/test/unit/setup.ts'],
    coverage: {
      provider: 'v8',
      include: ['src/core/**/*.ts', 'src/infra/**/*.ts', 'src/*.ts'],
      exclude: [
        'src/test/**',
        'src/**/*.test.ts',
        'src/**/*.d.ts',
        'src/core/dtos/**',
        'src/infra/view/js/**',
        'src/infra/view/html/**',
        '**/node_modules/**',
      ],
      reporter: ['html', 'lcov', 'text-summary'],
      reportsDirectory: './coverage/unit',
      thresholds: {
        lines: 95,
        statements: 95,
        functions: 95,
        branches: 90,
      },
    },
  },
});
