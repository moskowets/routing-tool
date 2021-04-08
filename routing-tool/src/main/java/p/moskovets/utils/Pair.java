package p.moskovets.utils;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class Pair<A, B> {
    @Expose
    public final A fst;
    @Expose
    public final B snd;

    public Pair(A fst, B snd) {
        this.fst = fst;
        this.snd = snd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<A, B> pair = (Pair<A, B>) o;
        return Objects.equals(fst, pair.fst) && Objects.equals(snd, pair.snd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fst, snd);
    }
}
