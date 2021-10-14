package sk.upjs.ics.kopr.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

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
        String sentence = calculateFrequencies.getSentence();
        getContext().getLog().info("Handling a sentence: '{}'", sentence);

        Map<String, Long> frequencies = Stream.of(sentence.split("\\s"))
                .collect(groupingBy(String::toString, counting()));

        getContext().getLog().info("Calculated frequencies: '{}'", frequencies);

        calculateFrequencies.getReplyTo().tell(new FrequenciesCalculated(frequencies));

        return Behaviors.same();
    }

    //-------------------

    public interface Command {}

    public interface Event {}

    public static class CalculateFrequencies implements Command {
        private final String sentence;

        private final ActorRef<FrequenciesCalculated> replyTo;

        public CalculateFrequencies(String sentence, ActorRef<FrequenciesCalculated> replyTo) {
            this.sentence = sentence;
            this.replyTo = replyTo;
        }

        public String getSentence() {
            return sentence;
        }

        public ActorRef<FrequenciesCalculated> getReplyTo() {
            return replyTo;
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
