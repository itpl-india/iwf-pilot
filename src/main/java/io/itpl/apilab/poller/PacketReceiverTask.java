package io.itpl.apilab.poller;

import io.itpl.apilab.data.Packet;
import io.itpl.apilab.receiver.PacketReceiver;

public class PacketReceiverTask implements Runnable{
    private Packet packet;
    private PacketReceiver collector;

    public PacketReceiverTask(Packet packet, PacketReceiver collector) {
        this.packet = packet;
        this.collector = collector;
    }

    @Override
    public void run() {
        collector.receive(packet);
    }
}
