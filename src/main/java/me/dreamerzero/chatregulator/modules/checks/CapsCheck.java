package me.dreamerzero.chatregulator.modules.checks;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.result.ReplaceableResult;

/**
 * Check for compliance with uppercase character limit in a string
 */
public class CapsCheck implements ICheck {
    private final int limit;

    CapsCheck(){
        this.limit = Configuration.getConfig().getCapsConfig().limit();
    }

    CapsCheck(int limit){
        this.limit = limit;
    }

    @Override
    public CompletableFuture<Result> check(@NotNull String string) {
        char[] chararray = Objects.requireNonNull(string).toCharArray();
        int capcount = 0;
        for(char c : chararray){
            if(Character.isUpperCase(c)) capcount++;
        }

        if(capcount >= limit){
            return CompletableFuture.completedFuture(new Result(string, true));
        }
        return CompletableFuture.completedFuture(new ReplaceableResult(string, false){
            @Override
            public String replaceInfraction(){
                return string.toLowerCase(Locale.ROOT);
            }
        });
    }

    public static CompletableFuture<Result> createCheck(String string){
        return new CapsCheck().check(string);
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.CAPS;
    }

    public static CapsCheck.Builder builder(){
        return new CapsCheck.Builder();
    }

    public static class Builder{
        private int limit;
        Builder(){}

        public Builder limit(int limit){
            this.limit = limit;
            return this;
        }

        public CapsCheck build(){
            return limit == 0 ? new CapsCheck() : new CapsCheck(limit);
        }
    }
}
