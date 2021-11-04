## About this Fork

This is a fork of Java Modbus Library jamod (now abandoded) - http://jamod.sourceforge.net/. Original SVN revision at the time of fork was 26 according to https://svn.code.sf.net/p/jamod/svn/trunk/.

The code originally lived in openhab1-addons (binding) and openhab2-addons (transport) repos. Since then, the fork has been separated here, and the commits have been rebased on top of clean jamod SVN checkout. Further details how this has been done is documented in [openHAB2 Modbus binding PR](https://github.com/openhab/openhab-addons/pull/2246#issuecomment-341983287).

**NOTE:** This fork is very passively maintained to meet the needs of [openHAB](https://www.openhab.org/), and mainly receives critical bug fixes in the context of openHAB.
The library is abstracted fully in openHAB by the modbus transport bundle ([org.openhab.core.io.transport.modbus in openhab-core](https://github.com/openhab/openhab-core/tree/main/bundles/org.openhab.core.io.transport.modbus)).

Readers seeking a general purpose modbus library in java are invited to familiarize with more actively maintained forks such as [steveohara/j2mod](https://github.com/steveohara/j2mod/). The fork living in this repo is not recommended to be used outside openHAB context.

## About

This project represents a Modbus implementation in 100% Java. It can be used to implement Modbus masters and slaves in various flavors:

- Serial: ASCII, RTU (Master only), BIN
- IP: TCP, UDP, RTU/IP (Master Only)

The design of this library is fully object oriented, based on abstractions which should support easy understanding, reusability and extensibility.

One important goal of this project is a codebase that is easily usable on a variety of Java Platforms (and devices). Many limited resource devices do not provide Java 5 and Java 6 environments, and there are only limited possibilities for logging.
