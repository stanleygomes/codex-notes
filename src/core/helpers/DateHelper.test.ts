import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { DateHelper } from './DateHelper';

const NOW = new Date(2026, 9, 15, 14, 30, 45).getTime();
const SECOND = 1000;
const MINUTE = 60 * SECOND;
const HOUR = 60 * MINUTE;
const DAY = 24 * HOUR;

describe('DateHelper', () => {
  beforeEach(() => {
    vi.useFakeTimers();
    vi.setSystemTime(NOW);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it.each([
    [NOW - 5 * SECOND, 'just now'],
    [NOW - 30 * SECOND, '30 seconds ago'],
    [NOW - 5 * MINUTE, '5 minutes ago'],
    [NOW - 3 * HOUR, '3 hours ago'],
    [NOW - 2 * DAY, '2 days ago'],
    [NOW - 60 * DAY, '2 months ago'],
    [NOW - 800 * DAY, '2 years ago'],
    [NOW + 2 * HOUR, 'in 2 hours'],
  ])('formats %d as "%s"', (timestamp, expected) => {
    expect(DateHelper.toHumanRelative(timestamp)).toBe(expected);
  });

  it('returns the start of the day', () => {
    expect(DateHelper.startOfDay(new Date(NOW))).toEqual(
      new Date(2026, 9, 15, 0, 0, 0, 0),
    );
  });

  it('returns the sunday that starts the week', () => {
    expect(DateHelper.startOfWeek(new Date(NOW))).toEqual(
      new Date(2026, 9, 11, 0, 0, 0, 0),
    );
  });

  it('returns the first day of the month', () => {
    expect(DateHelper.startOfMonth(new Date(NOW))).toEqual(
      new Date(2026, 9, 1, 0, 0, 0, 0),
    );
  });

  it('does not mutate the given date', () => {
    const date = new Date(NOW);

    DateHelper.startOfWeek(date);

    expect(date.getTime()).toBe(NOW);
  });

  it('returns the current timestamp', () => {
    expect(DateHelper.nowMs()).toBe(NOW);
  });
});
