package zlc.season.morbidmaskapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import zlc.season.morbidmask.annotation.Params;
import zlc.season.morbidmask.annotation.Val;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

@Params({
        @Val(key = "byteParam", type = Byte.class),
        @Val(key = "shortParam", type = Short.class),
        @Val(key = "intParam", type = Integer.class),
        @Val(key = "longParam", type = Long.class),
        @Val(key = "floatParam", type = Float.class),
        @Val(key = "doubleParam", type = Double.class),
        @Val(key = "charParam", type = Character.class),
        @Val(key = "booleanParam", type = Boolean.class),
        @Val(key = "stringParam", type = String.class),
        @Val(key = "customParam", type = CustomEntity.class),
        @Val(key = "parcelable", type = ParcelableEntity.class),
        @Val(key = "serializable", type = SerializableEntity.class)
})
public class JavaDialogFragment extends DialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        JavaDialogFragmentParams params = JavaDialogFragmentParams.Companion.of(this);

        TextView textView = view.findViewById(R.id.tv_content);

        String content = "Params:" +
                "byteParams =" + params.getByteParam() + "\n" +
                "shortParam =" + params.getShortParam() + "\n" +
                "intParam =" + params.getIntParam() + "\n" +
                "longParam =" + params.getLongParam() + "\n" +
                "floatParam =" + params.getFloatParam() + "\n" +
                "doubleParam =" + params.getDoubleParam() + "\n" +
                "charParam =" + params.getCharParam() + "\n" +
                "booleanParam =" + params.getByteParam() + "\n" +
                "stringParam =" + params.getStringParam() + "\n" +
                "customParam = " + params.getCustomParam().toString() + "\n" +
                "parcelable = " + params.getParcelable().toString() + "\n" +
                "serializable = " + params.getSerializable().toString() + "\n";

        textView.setText(content);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setLayout(MATCH_PARENT, MATCH_PARENT);
    }
}
