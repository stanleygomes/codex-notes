"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.FilenameHelper = void 0;
class FilenameHelper {
    static normalize(title) {
        return title
            .replace(/[/\\:*?"<>|]/g, '')
            .replace(/[^\x20-\x7E]/g, '')
            .trim()
            .replace(/\s+/g, ' ')
            .replace(/ /g, '-')
            .toLowerCase();
    }
}
exports.FilenameHelper = FilenameHelper;
//# sourceMappingURL=FilenameHelper.js.map