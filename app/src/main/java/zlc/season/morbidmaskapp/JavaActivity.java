package zlc.season.morbidmaskapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import zlc.season.morbidmask.annotation.Params;
import zlc.season.morbidmask.annotation.Val;

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
public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        JavaActivityParams params = JavaActivityParams.Companion.of(this);

        TextView textView = findViewById(R.id.tv_content);

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
}
