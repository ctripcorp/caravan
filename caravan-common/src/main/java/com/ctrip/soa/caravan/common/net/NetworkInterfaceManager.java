package com.ctrip.soa.caravan.common.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NetworkInterfaceManager {

    private static final Logger _logger = LoggerFactory.getLogger(NetworkInterfaceManager.class);

    public static final NetworkInterfaceManager INSTANCE = new NetworkInterfaceManager();

    private InetAddress _localIP;

    private String _localhostIP;
    private String _localhostName;

    private NetworkInterfaceManager() {
        try {
            _localIP = getLocalIP();
            if (_localIP == null)
                throw new RuntimeException("Cannot find local ipv4 address!");

            _localhostIP = _localIP.getHostAddress();
            _localhostName = getLocalHostName();
        } catch (Throwable ex) {
            _logger.error("IPv4 address or local hostName init failed!", ex);
        }
    }

    public String localhostIP() {
        if (_localhostIP == null)
            throw new RuntimeException("Cannot find local ipv4 address!");

        return _localhostIP;
    }

    public String localhostName() {
        if (_localhostName == null)
            throw new RuntimeException("Cannot find local hostName!");

        return _localhostName;
    }

    private String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Throwable ex) {
            _logger.warn("Cannot get hostName from InetAddress.getLocalHost()!", ex);
            return _localIP.getHostName();
        }
    }

    private InetAddress getLocalIP() throws SocketException, UnknownHostException {
        List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
        List<Inet4Address> ipV4Addresses = new ArrayList<>();
        try {
            for (NetworkInterface ni : nis) {
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual())
                    continue;

                List<InetAddress> list = Collections.list(ni.getInetAddresses());
                for (InetAddress address : list) {
                    if (address.isLoopbackAddress())
                        continue;

                    if (address instanceof Inet4Address)
                        ipV4Addresses.add((Inet4Address) address);
                }
            }
        } catch (Throwable ex) {
            _logger.error("Get local ip failed.", ex);
        }

        InetAddress address = getValidIPv4(ipV4Addresses);
        if (address == null)
            address = InetAddress.getLocalHost();
        return address;
    }

    private Inet4Address getValidIPv4(List<Inet4Address> addresses) {
        Inet4Address valid = null;

        int maxWeight = -1;
        for (Inet4Address address : addresses) {
            int weight = 0;

            if (!address.getHostName().equals(address.getHostAddress()))
                weight += 1;

            if (address.isLinkLocalAddress())
                weight += 4;

            if (address.isSiteLocalAddress())
                weight += 8;

            if (weight > maxWeight) {
                maxWeight = weight;
                valid = address;
            }
        }

        return valid;
    }

}
