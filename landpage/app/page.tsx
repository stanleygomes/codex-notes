import type { JSX } from "react";
import Image from "next/image";
import logo from "./img/logo.svg";

const GITHUB_URL = "https://github.com/stanleygomes/codex-notes";
const LICENSE_URL =
  "https://github.com/stanleygomes/codex-notes/blob/master/LICENSE";

interface DownloadButton {
  label: string;
  href: string;
  command: string;
  available: boolean;
}

const DOWNLOAD_BUTTONS: DownloadButton[] = [
  {
    label: "Visual Studio Code",
    href: "https://marketplace.visualstudio.com/items?itemName=StanleyGomes.codex-notes",
    command: "code --install-extension StanleyGomes.codex-notes",
    available: true,
  },
  {
    label: "Cursor",
    href: "https://open-vsx.org/extension/stanleygomes/codex-notes",
    command: "cursor --install-extension StanleyGomes.codex-notes",
    available: true,
  },
  {
    label: "Antigravity",
    href: "https://open-vsx.org/extension/stanleygomes/codex-notes",
    command: "antigravity --install-extension StanleyGomes.codex-notes",
    available: true,
  },
];

function DownloadButtonItem({ btn }: { btn: DownloadButton }): JSX.Element {
  if (!btn.available) {
    return (
      <div className="mb-4 opacity-50">
        <p className="text-[#33ff00] mb-1">$ {btn.command}</p>
        <p className="text-red-500">Error: Package not found (Coming soon)</p>
      </div>
    );
  }

  return (
    <div className="mb-4">
      <p className="text-[#33ff00] mb-1 flex flex-wrap items-center">
        <span className="text-[#22aa00] mr-2">$</span>
        <a
          href={btn.href}
          target="_blank"
          rel="noopener noreferrer"
          className="hover:bg-[#33ff00] hover:text-black transition-colors break-all"
        >
          {btn.command}
        </a>
      </p>
      <p className="text-[#115500] text-sm">
        # Install for{" "}
        <a
          href={btn.href}
          target="_blank"
          rel="noopener noreferrer"
          className="underline decoration-dotted hover:text-[#22aa00] transition-colors"
        >
          {btn.label}
        </a>
      </p>
    </div>
  );
}

export default function Home(): JSX.Element {
  return (
    <div className="min-h-screen p-4 sm:p-8 font-mono bg-black relative overflow-hidden">
      <main className="max-w-4xl mx-auto border border-[#33ff00] p-6 bg-black shadow-[0_0_15px_rgba(51,255,0,0.3)] relative z-10">
        <div className="mb-8 border-b border-[#33ff00] pb-4">
          <div className="flex flex-col md:flex-row gap-6 items-start">
            <div className="flex-1 w-full overflow-hidden">
              <pre className="text-[#33ff00] text-[10px] sm:text-xs md:text-sm overflow-x-auto whitespace-pre-wrap leading-tight">
                {`
  ____           _             _   _       _            
 / ___|___   __| | _____  __ | \\ | | ___ | |_ ___  ___ 
| |   / _ \\ / _\` |/ _ \\ \\/ / |  \\| |/ _ \\| __/ _ \\/ __|
| |__| (_) | (_| |  __/>  <  | |\\  | (_) | ||  __/\\__ \\
 \\____\\___/ \\__,_|\\___/_/\\_\\ |_| \\_\\|___/ \\__\\___||___/
`}
              </pre>
            </div>
            <div className="relative shrink-0 border border-[#33ff00] p-1 bg-[#051100]">
              <Image
                src={logo}
                alt="Codex Notes logo"
                width={80}
                height={80}
                className="grayscale contrast-[200%] brightness-[1.2] sepia-[1] hue-rotate-[60deg] mix-blend-screen opacity-90"
                priority
              />
              <div className="absolute inset-0 bg-[repeating-linear-gradient(0deg,rgba(0,0,0,0.15),rgba(0,0,0,0.15)_1px,transparent_1px,transparent_2px)] pointer-events-none"></div>
            </div>
          </div>

          <div className="mt-4 space-y-2">
            <p className="text-[#33ff00] animate-pulse">
              &gt; System initialized...
            </p>
            <p className="text-[#33ff00]">
              &gt; Description: A powerful plugin for managing notes within your IDE. organizing thoughts and code snippets.
            </p>
            <p className="text-[#33ff00]">
              &gt; Status: Online. Ready for download.
            </p>
          </div>
        </div>

        <div className="mb-8">
          <p className="text-[#22aa00] mb-4">root@codex-notes:~# ls -l downloads/</p>
          <div className="pl-2 sm:pl-4 border-l-2 border-[#115500]">
            {DOWNLOAD_BUTTONS.map((btn) => (
              <DownloadButtonItem key={btn.label} btn={btn} />
            ))}
          </div>
        </div>

        <div className="mt-12 pt-4 border-t border-[#115500] flex flex-col sm:flex-row justify-between items-start sm:items-center text-sm text-[#22aa00]">
          <p>root@codex-notes:~# echo "Made with 🔥 by NazarethLabs"</p>
          <div className="flex gap-4 mt-4 sm:mt-0">
            <a
              href={GITHUB_URL}
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-[#33ff00] hover:bg-[#115500] px-1 transition-colors"
            >
              [src/github]
            </a>
            <a
              href={LICENSE_URL}
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-[#33ff00] hover:bg-[#115500] px-1 transition-colors"
            >
              [sys/license]
            </a>
          </div>
        </div>
      </main>

      {/* Scanline effect overlay */}
      <div className="pointer-events-none fixed inset-0 z-50 bg-[linear-gradient(rgba(18,16,16,0)_50%,rgba(0,0,0,0.25)_50%),linear-gradient(90deg,rgba(255,0,0,0.06),rgba(0,255,0,0.02),rgba(0,0,255,0.06))] bg-[length:100%_4px,3px_100%] opacity-20"></div>
    </div>
  );
}
