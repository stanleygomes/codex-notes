CREATE TABLE `notes` (
	`id` text PRIMARY KEY NOT NULL,
	`title` text NOT NULL,
	`filePath` text NOT NULL,
	`createdAt` integer NOT NULL,
	`updatedAt` integer NOT NULL,
	`isFavorite` integer DEFAULT 0 NOT NULL,
	`color` text DEFAULT 'NONE' NOT NULL
);
