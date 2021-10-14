package sk.upjs.ics.kopr.akka;

import akka.actor.typed.ActorSystem;

public class Runner {
    public static void main(String[] args) {
        var system = ActorSystem.create(WordFrequencyCounter.create(), "system");
        system.tell(new WordFrequencyCounter.CalculateFrequencies("Hello World"));
        system.tell(new WordFrequencyCounter.CalculateFrequencies("Hola, mundo!"));
    }
}
