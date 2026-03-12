import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Codex Notes",
  description:
    "A powerful plugin for managing notes within your IDE. Keep your thoughts, code snippets, and documentation organized without leaving your development environment.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className="antialiased">{children}</body>
    </html>
  );
}
