import { describe, expect, it } from 'vitest';
import { buildNote } from '../../test/unit/fakes';
import { SearchHelper } from './SearchHelper';

const notes = [
  buildNote({ id: 'meeting', title: 'Meeting notes' }),
  buildNote({ id: 'api', title: 'API design' }),
  buildNote({ id: 'groceries', title: 'groceries-list' }),
];

function ids(result: { id: string }[]): string[] {
  return result.map((note) => note.id);
}

describe('SearchHelper', () => {
  it.each(['', '   '])('returns every note for blank query "%s"', (query) => {
    expect(SearchHelper.search(notes, query)).toBe(notes);
  });

  it('matches titles ignoring case and surrounding spaces', () => {
    expect(ids(SearchHelper.search(notes, '  MEETING '))).toEqual(['meeting']);
  });

  it('matches individual words split by separators', () => {
    expect(ids(SearchHelper.search(notes, 'list'))).toEqual(['groceries']);
  });

  it('drops notes without any match', () => {
    expect(SearchHelper.search(notes, 'zzz')).toEqual([]);
  });

  it('ranks exact title matches above partial ones', () => {
    const list = [
      buildNote({ id: 'partial', title: 'api design doc' }),
      buildNote({ id: 'exact', title: 'api' }),
    ];

    expect(ids(SearchHelper.search(list, 'api'))).toEqual(['exact', 'partial']);
  });

  it('matches note content', () => {
    const content = new Map([['api', 'Talk about REST endpoints']]);

    expect(ids(SearchHelper.search(notes, 'rest endpoints', content))).toEqual([
      'api',
    ]);
  });

  it('ranks title matches above content-only matches', () => {
    const content = new Map([['meeting', 'we discussed the api']]);

    expect(ids(SearchHelper.search(notes, 'api', content))).toEqual([
      'api',
      'meeting',
    ]);
  });

  it('ignores blank content', () => {
    const content = new Map([['api', '   ']]);

    expect(SearchHelper.search(notes, 'zzz', content)).toEqual([]);
  });
});
