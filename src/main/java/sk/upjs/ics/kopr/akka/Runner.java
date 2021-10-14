package sk.upjs.ics.kopr.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import akka.japi.function.Function;
import sk.upjs.ics.kopr.akka.WordFrequencyCounter.CalculateFrequencies;
import sk.upjs.ics.kopr.akka.WordFrequencyCounter.FrequenciesCalculated;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class Runner {
    public static class SentenceMessageFactory implements Function<ActorRef<FrequenciesCalculated>, WordFrequencyCounter.Command> {
        private final String sentence;

        public SentenceMessageFactory(String sentence) {
            this.sentence = sentence;
        }

        @Override
        public CalculateFrequencies apply(ActorRef<FrequenciesCalculated> replyTo) {
            return new CalculateFrequencies(this.sentence, replyTo);
        }
    }

    public static void main(String[] args) {
        ActorSystem<WordFrequencyCounter.Command> system = ActorSystem.create(WordFrequencyCounter.create(), "system");
        CompletionStage<WordFrequencyCounter.FrequenciesCalculated> result = AskPattern.ask(system, new SentenceMessageFactory("Hello World!"), Duration.ofSeconds(5), system.scheduler());
        result
                .thenApply(FrequenciesCalculated::getFrequencies)
                .thenAccept(System.out::println);
    }
}
