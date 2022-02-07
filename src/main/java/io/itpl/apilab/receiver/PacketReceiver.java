package io.itpl.apilab.receiver;

import io.itpl.apilab.data.Packet;

public interface PacketReceiver {
    public void receive(Packet packet);
}
