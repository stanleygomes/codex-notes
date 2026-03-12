import { formatDistanceToNow } from 'date-fns';

export class DateHelper {
  static toHumanRelative(timestampMs: number): string {
    return formatDistanceToNow(new Date(timestampMs), { addSuffix: true });
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
