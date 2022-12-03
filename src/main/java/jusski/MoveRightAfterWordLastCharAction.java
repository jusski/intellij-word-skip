package jusski;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MoveRightAfterWordLastCharAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();

        int end = editor.getDocument().getLineEndOffset(caretModel.getLogicalPosition().line) + 1;
        CharSequence chars = editor.getDocument().getCharsSequence();

        moveCaretRightToWordEnd(caretModel, chars, end);

    }

    private int moveCaretRightToWordStart(CaretModel caret, CharSequence chars, int end) {
        int offset = caret.getOffset();

        if (Character.isJavaIdentifierPart(chars.charAt(offset))) {
            offset = moveCaretRightToWordEnd(caret, chars, end);
        }

        for (; offset < end; ++offset) {
            char c = chars.charAt(offset);
            if (Character.isJavaIdentifierStart(c)) {
                caret.moveToOffset(offset);
                break;
            }
        }

        return offset;
    }

    private int moveCaretRightToWordEnd(CaretModel caret, CharSequence chars, int end) {
        int offset = caret.getOffset();

        if (!Character.isJavaIdentifierPart(chars.charAt(offset))) {
            offset = moveCaretRightToWordStart(caret, chars, end);
        }

        for (; offset < end; ++offset) {
            char c = chars.charAt(offset);
            if (!Character.isJavaIdentifierPart(c)) {
                caret.moveToOffset(offset);
                break;
            }
        }

        return offset;
    }

    @Override
    public void update(AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        e.getPresentation().setEnabledAndVisible(project != null && editor != null);
    }

}
