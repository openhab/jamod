This is a fork of Java Modbus Library (jamod) - http://jamod.sourceforge.net/

## About

This project represents a Modbus implementation in 100% Java. It can be used to implement Modbus masters and slaves in various flavors:

- Serial: ASCII, RTU (Master only), BIN
- IP: TCP, UDP

The design of this library is fully object oriented, based on abstractions which should support easy understanding, reusability and extensibility.

One important goal of this project is a codebase that is easily usable on a variety of Java Platforms (and devices). Many limited resource devices do not provide Java 5 and Java 6 environments, and there are only limited possibilities for logging.
