package jusski;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MoveLeftToWordFirstCharAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();

        int start = editor.getDocument().getLineStartOffset(caretModel.getLogicalPosition().line);
        CharSequence chars = editor.getDocument().getCharsSequence();

        moveCaretLeftToWordFirstChar(caretModel, chars, start);
    }

    private void moveCaretLeftToWordFirstChar(CaretModel caret, CharSequence chars, int start) {
        int offset = caret.getOffset();
        if (start != offset) {
            offset -= 1;
        }

        if (!Character.isJavaIdentifierPart(chars.charAt(offset))) {
            offset = moveCaretLeftToJavaIdentifierChar(caret, chars, start, offset);
        }

        for (; offset >= start; --offset) {
            if(offset - 1 >= start) {
                char c = chars.charAt(offset - 1);
                if (!Character.isJavaIdentifierStart(c)) {
                    caret.moveToOffset(offset);
                    break;
                }
            } else {
                char c = chars.charAt(offset);
                if (Character.isJavaIdentifierStart(c)) {
                    caret.moveToOffset(offset);
                    break;
                }
            }

        }
    }

    private int moveCaretLeftToJavaIdentifierChar(CaretModel caret, CharSequence chars, int start, int offset) {
        for (; offset >= start; --offset) {
            char c = chars.charAt(offset);
            if (Character.isJavaIdentifierPart(c)) {
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
