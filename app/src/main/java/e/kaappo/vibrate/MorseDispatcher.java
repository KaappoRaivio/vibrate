package e.kaappo.vibrate;

import android.content.Context;
import android.os.AsyncTask;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

public class MorseDispatcher extends AsyncTask<Void, Void, Void> {
    final private Queue<Morse> queue = new LinkedList<>();
    final private Vibrator vibrator;
    private final MainActivity context;

    private long previousAction = -1;

    MorseDispatcher (Vibrator vibrator, MainActivity context) {
        super();
        this.vibrator = vibrator;
        this.context = context;

        previousAction = System.currentTimeMillis();
    }


    public void addMorse (final Morse morse) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - previousAction > Morse.INTER_CHARACTER_PAUSE.getLength()) {
            queue.add(Morse.INTER_CHARACTER_PAUSE);
        }

        queue.add(morse);
        synchronized (queue) {
            queue.notifyAll();
        }

        previousAction = System.currentTimeMillis();

        ((TextView) context.findViewById(R.id.result)).setText(queue.stream().map(Morse::getSymbol).collect(Collectors.joining()));

    }

    @Override
    protected Void doInBackground (Void... voids) {
        while (true) {
            if (isCancelled()) {
                return null;
            }

            waitForMorse();

            vibrateAndBlock(Optional.of(queue.remove()).get());
            try {
                Thread.sleep(Morse.INTRA_CHAR_PAUSE.getLength());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForMorse () {
        while (queue.isEmpty()) {
            synchronized (queue) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void vibrateAndBlock (Morse length) {
        if (length == Morse.INTER_CHARACTER_PAUSE) {
            try {
                Thread.sleep(Morse.INTER_CHARACTER_PAUSE.getLength() - Morse.INTRA_CHAR_PAUSE.getLength());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (length != Morse.INTRA_CHAR_PAUSE) {
            vibrator.vibrate(VibrationEffect.createOneShot(length.getLength(), VibrationEffect.DEFAULT_AMPLITUDE));
            try {
                Thread.sleep(length.getLength());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
