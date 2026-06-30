export class DateHelper {
  static toHumanRelative(timestampMs: number): string {
    const diffMs = timestampMs - Date.now();
    const diffSecs = Math.round(diffMs / 1000);
    const diffMins = Math.round(diffMs / 60000);
    const diffHours = Math.round(diffMs / 3600000);
    const diffDays = Math.round(diffMs / 86400000);
    const diffMonths = Math.round(diffMs / 2592000000); // approx 30 days
    const diffYears = Math.round(diffMs / 31536000000); // approx 365 days

    const rtf = new Intl.RelativeTimeFormat('en', { numeric: 'always' });

    const absSecs = Math.abs(diffSecs);
    const absMins = Math.abs(diffMins);
    const absHours = Math.abs(diffHours);
    const absDays = Math.abs(diffDays);
    const absMonths = Math.abs(diffMonths);

    if (absSecs < 60) {
      return absSecs < 10 ? 'just now' : rtf.format(diffSecs, 'second');
    }
    if (absMins < 60) {
      return rtf.format(diffMins, 'minute');
    }
    if (absHours < 24) {
      return rtf.format(diffHours, 'hour');
    }
    if (absDays < 30) {
      return rtf.format(diffDays, 'day');
    }
    if (absMonths < 12) {
      return rtf.format(diffMonths, 'month');
    }
    return rtf.format(diffYears, 'year');
  }

  static startOfDay(date: Date): Date {
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  static startOfWeek(date: Date): Date {
    const d = new Date(date);
    const day = d.getDay();
    d.setDate(d.getDate() - day);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  static startOfMonth(date: Date): Date {
    const d = new Date(date);
    d.setDate(1);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  static nowMs(): number {
    return Date.now();
  }
}
