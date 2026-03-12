import type { JSX } from "react";

const GITHUB_URL = "https://github.com/stanleygomes/codex-notes";
const LICENSE_URL =
  "https://github.com/stanleygomes/codex-notes/blob/master/LICENSE";

interface DownloadButton {
  label: string;
  href: string;
  bgColor: string;
  textColor: string;
  available: boolean;
}

const DOWNLOAD_BUTTONS: DownloadButton[] = [
  {
    label: "JetBrains IDEs",
    href: "https://plugins.jetbrains.com/plugin/30177-codex-notes",
    bgColor: "bg-[#000000]",
    textColor: "text-white",
    available: true,
  },
  {
    label: "Visual Studio Code",
    href: "#",
    bgColor: "bg-[#007ACC]",
    textColor: "text-white",
    available: false,
  },
  {
    label: "Cursor",
    href: "#",
    bgColor: "bg-[#6B21A8]",
    textColor: "text-white",
    available: false,
  },
];

function Logo(): JSX.Element {
  return (
    <div className="border-4 border-black shadow-[6px_6px_0px_#000] bg-white p-4 inline-block">
      <svg
        width="80"
        height="80"
        viewBox="0 0 80 80"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        aria-label="Codex Notes logo"
      >
        <rect width="80" height="80" fill="#FBBF24" />
        <rect
          x="16"
          y="12"
          width="48"
          height="56"
          rx="2"
          fill="white"
          stroke="black"
          strokeWidth="3"
        />
        <line
          x1="24"
          y1="28"
          x2="56"
          y2="28"
          stroke="black"
          strokeWidth="3"
          strokeLinecap="round"
        />
        <line
          x1="24"
          y1="38"
          x2="56"
          y2="38"
          stroke="black"
          strokeWidth="3"
          strokeLinecap="round"
        />
        <line
          x1="24"
          y1="48"
          x2="44"
          y2="48"
          stroke="black"
          strokeWidth="3"
          strokeLinecap="round"
        />
        <circle cx="24" cy="18" r="3" fill="black" />
        <circle cx="32" cy="18" r="3" fill="black" />
        <circle cx="40" cy="18" r="3" fill="black" />
      </svg>
    </div>
  );
}

function DownloadButtonItem({ btn }: { btn: DownloadButton }): JSX.Element {
  const baseClasses =
    "border-4 border-black font-black text-lg px-8 py-4 transition-all duration-100 block text-center w-full max-w-sm";

  if (!btn.available) {
    return (
      <div
        className={`${baseClasses} ${btn.bgColor} ${btn.textColor} opacity-50 cursor-not-allowed shadow-[4px_4px_0px_#000]`}
        title="Coming soon"
      >
        {btn.label}
        <span className="block text-sm font-bold mt-1 opacity-80">
          Coming soon
        </span>
      </div>
    );
  }

  return (
    <a
      href={btn.href}
      target="_blank"
      rel="noopener noreferrer"
      className={`${baseClasses} ${btn.bgColor} ${btn.textColor} shadow-[4px_4px_0px_#000] hover:shadow-none hover:translate-x-[4px] hover:translate-y-[4px] active:shadow-none`}
    >
      {btn.label}
    </a>
  );
}

export default function Home(): JSX.Element {
  return (
    <div className="min-h-screen bg-[#fef9c3] flex flex-col">
      <main className="flex-1 flex flex-col items-center justify-center px-6 py-16 gap-10">
        <Logo />

        <div className="text-center max-w-2xl">
          <h1 className="text-6xl sm:text-7xl font-black text-black tracking-tight border-b-4 border-black pb-2 mb-6 inline-block">
            Codex Notes
          </h1>
          <p className="text-xl font-bold text-black leading-relaxed border-4 border-black bg-white shadow-[6px_6px_0px_#000] px-6 py-4">
            A powerful plugin for managing notes within your IDE. Keep your
            thoughts, code snippets, and documentation organized without leaving
            your development environment.
          </p>
        </div>

        <div className="flex flex-col items-center gap-4 w-full max-w-sm">
          <h2 className="text-2xl font-black text-black uppercase tracking-widest border-b-4 border-black pb-1 w-full text-center">
            Download
          </h2>
          {DOWNLOAD_BUTTONS.map((btn) => (
            <DownloadButtonItem key={btn.label} btn={btn} />
          ))}
        </div>
      </main>

      <footer className="border-t-4 border-black bg-white px-6 py-6 flex flex-col sm:flex-row items-center justify-between gap-4">
        <p className="font-black text-black text-base">
          Made with 🔥 by NazarethLabs
        </p>
        <div className="flex gap-6">
          <a
            href={GITHUB_URL}
            target="_blank"
            rel="noopener noreferrer"
            className="font-black text-black underline decoration-4 hover:bg-black hover:text-[#fef9c3] px-2 py-1 transition-colors duration-100"
          >
            GitHub
          </a>
          <a
            href={LICENSE_URL}
            target="_blank"
            rel="noopener noreferrer"
            className="font-black text-black underline decoration-4 hover:bg-black hover:text-[#fef9c3] px-2 py-1 transition-colors duration-100"
          >
            License
          </a>
        </div>
      </footer>
    </div>
  );
}
