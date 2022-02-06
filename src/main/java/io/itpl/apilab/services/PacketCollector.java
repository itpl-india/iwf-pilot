package io.itpl.apilab.services;

import io.itpl.apilab.data.Packet;

public interface PacketCollector {
    public void collect(Packet packet);
}
