package com.saki.ui.method;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class NinePasswordTransformationMethod extends
        PasswordTransformationMethod {
    private class NineSequence implements CharSequence {
        char[] code = {'①', '②', '③', '④', '⑤', '⑥', '⑦', '⑧', '⑨', '⑩', '⑪',
                '⑫', '⑬', '⑭', '⑮', '⑯', '⑰', '⑱', '⑲', '⑳'};
        private CharSequence sequen;

        public NineSequence(CharSequence src) {
            sequen = src;
        }

        @Override
        public char charAt(int index) {
            return code[index % code.length];
        }

        @Override
        public int length() {
            return sequen.length();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return sequen.subSequence(start, end);
        }

    }

    ;

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new NineSequence(source);
    }
}
