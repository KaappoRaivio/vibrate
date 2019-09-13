package e.kaappo.vibrate;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Vibrator vibrator;
    private MorseDispatcher dispatcher;

    @Override
    protected void onDestroy () {
        dispatcher.cancel(true);
        super.onDestroy();
    }

    @Override
    protected void onPause () {
        dispatcher.cancel(true);
        super.onPause();
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        dispatcher = new MorseDispatcher(vibrator, this);
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                System.out.println("Volume up pressed");
                dispatcher.addMorse(Morse.DASH);
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                System.out.println("Volume down pressed");
                dispatcher.addMorse(Morse.DIT);
                return true;

            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp (int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                System.out.println("Volume up released");
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                System.out.println("Volume down released");
//                vibrator.cancel();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public void submit (View view) {

        Morse.DIT.setLength(Long.parseLong(((TextView) findViewById(R.id.multiplier)).getText().toString()));
        Morse.INTRA_CHAR_PAUSE.setLength(Long.parseLong(((TextView) findViewById(R.id.multiplier)).getText().toString()));
        Morse.DASH.setLength(Long.parseLong(((TextView) findViewById(R.id.multiplier)).getText().toString()) * 3);
        Morse.INTER_CHARACTER_PAUSE .setLength(Long.parseLong(((TextView) findViewById(R.id.multiplier)).getText().toString()) * 3);
    }

    public void confirm (View view) {
        System.out.println("Confirming");
        dispatcher.execute();
        System.out.println("Confirmed");

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        dispatcher = new MorseDispatcher(vibrator, this);
    }
}
