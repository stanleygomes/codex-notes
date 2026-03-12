"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ChangeNoteColorService = void 0;
const NoteColorEnum_1 = require("../enum/NoteColorEnum");
const DialogHelper_1 = require("../helper/DialogHelper");
class ChangeNoteColorService {
    repository;
    constructor(repository) {
        this.repository = repository;
    }
    async changeColor(note) {
        const options = [
            { label: 'None', value: NoteColorEnum_1.NoteColorEnum.NONE },
            { label: 'Blue', value: NoteColorEnum_1.NoteColorEnum.BLUE },
            { label: 'Green', value: NoteColorEnum_1.NoteColorEnum.GREEN },
            { label: 'Yellow', value: NoteColorEnum_1.NoteColorEnum.YELLOW },
            { label: 'Orange', value: NoteColorEnum_1.NoteColorEnum.ORANGE },
            { label: 'Pink', value: NoteColorEnum_1.NoteColorEnum.PINK },
            { label: 'Red', value: NoteColorEnum_1.NoteColorEnum.RED },
            { label: 'Purple', value: NoteColorEnum_1.NoteColorEnum.PURPLE },
        ];
        const selected = await DialogHelper_1.DialogHelper.showQuickPick(options.map((o) => ({ label: o.label, description: o.value, value: o.value })), { placeHolder: 'Select note color' });
        if (!selected) {
            return;
        }
        this.repository.changeColor(note.id, selected.value);
    }
}
exports.ChangeNoteColorService = ChangeNoteColorService;
//# sourceMappingURL=ChangeNoteColorService.js.map