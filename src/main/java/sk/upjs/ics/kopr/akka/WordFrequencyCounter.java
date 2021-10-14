package sk.upjs.ics.kopr.akka;

public class WordFrequencyCounter {

    public interface Command {}

    public static class CalculateFrequencies implements Command {
        private final String sentence;

        public CalculateFrequencies(String sentence) {
            this.sentence = sentence;
        }

        public String getSentence() {
            return sentence;
        }
    }

}
