package io.itpl.apilab.broker;

import io.itpl.apilab.data.Packet;
import io.itpl.apilab.services.PacketCollector;

public class PacketCollectionTask implements Runnable{
    private Packet packet;
    private PacketCollector collector;

    public PacketCollectionTask(Packet packet, PacketCollector collector) {
        this.packet = packet;
        this.collector = collector;
    }

    @Override
    public void run() {
        collector.collect(packet);
    }
}
