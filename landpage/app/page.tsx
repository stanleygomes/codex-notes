import type { JSX } from "react";
import Image from "next/image";
import logo from "./img/logo.svg";

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
    bgColor: "bg-[#F8025E]",
    textColor: "text-white",
    available: true,
  },
  {
    label: "Visual Studio Code",
    href: "#",
    bgColor: "bg-[#0078D4]",
    textColor: "text-white",
    available: false,
  },
  {
    label: "Cursor",
    href: "#",
    bgColor: "bg-[#E3C25F]",
    textColor: "text-white",
    available: false,
  },
];

function Logo(): JSX.Element {
  return (
    <div className="p-4 inline-block">
      <Image
        src={logo}
        alt="Codex Notes logo"
        width={120}
        height={120}
        priority
      />
    </div>
  );
}

function DownloadButtonItem({ btn }: { btn: DownloadButton }): JSX.Element {
  const baseClasses =
    "border-4 border-black font-black text-lg px-8 py-4 transition-all duration-100 block text-center w-full max-w-sm rounded-xl";

  if (!btn.available) {
    return (
      <div
        className={`${baseClasses} ${btn.bgColor} ${btn.textColor} opacity-50 cursor-not-allowed shadow-[5px_5px_0px_#000]`}
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
      className={`${baseClasses} ${btn.bgColor} ${btn.textColor} shadow-[5px_5px_0px_#000] hover:shadow-none hover:shadow-none active:shadow-none`}
    >
      {btn.label}
    </a>
  );
}

export default function Home(): JSX.Element {
  return (
    <div className="min-h-screen flex flex-col bg-secondary-background bg-[linear-gradient(to_right,#80808033_1px,transparent_1px),linear-gradient(to_bottom,#80808033_1px,transparent_1px)] bg-[size:70px_70px] z-0">
      <main className="flex-1 flex flex-col items-center justify-center py-16 gap-10 px-6 mx-2">
        <div className="border-4 border-black bg-white shadow-[10px_10px_0px_#000] rounded-xl py-16 w-full max-w-3xl flex flex-col items-center gap-10">
          <Logo />

          <div className="text-center max-w-2xl">
            <h1 className="text-6xl sm:text-7xl font-black text-black tracking-tight border-b-4 border-black pb-2 mb-6 inline-block">
              Codex Notes
            </h1>
            <p className="text-xl text-black leading-relaxed px-6 py-4">
              A powerful plugin for managing notes within your IDE. Keep your
              thoughts, code snippets, and documentation organized without leaving
              your development environment.
            </p>
          </div>

          <div className="flex flex-col items-center gap-4 w-full max-w-sm">
            <h2 className="text-2xl font-black text-black uppercase tracking-widest pb-1 w-full text-center">
              Download for your favorite IDE
            </h2>
            {DOWNLOAD_BUTTONS.map((btn) => (
              <DownloadButtonItem key={btn.label} btn={btn} />
            ))}
          </div>
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
