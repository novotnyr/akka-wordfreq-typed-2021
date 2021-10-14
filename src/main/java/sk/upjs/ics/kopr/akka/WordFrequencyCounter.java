package sk.upjs.ics.kopr.akka;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class WordFrequencyCounter extends AbstractBehavior<WordFrequencyCounter.Command> {

    private WordFrequencyCounter(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(WordFrequencyCounter::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(CalculateFrequencies.class, this::calculateFrequencies)
                .build();
    }

    private Behavior<Command> calculateFrequencies(CalculateFrequencies calculateFrequencies) {
        getContext().getLog().info("Handling a sentence: '{}'", calculateFrequencies.getSentence());
        return Behaviors.same();
    }

    //-------------------

    public interface Command {}

    public interface Event {}

    public static class CalculateFrequencies implements Command {
        private final String sentence;

        public CalculateFrequencies(String sentence) {
            this.sentence = sentence;
        }

        public String getSentence() {
            return sentence;
        }
    }

    public static class FrequenciesCalculated implements Event {
        private final Map<String, Long> frequencies = new LinkedHashMap<>();

        public FrequenciesCalculated(Map<String, Long> frequencies) {
            this.frequencies.putAll(frequencies);
        }

        public Map<String, Long> getFrequencies() {
            return Collections.unmodifiableMap(frequencies);
        }
    }
}
