import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.core.spi.FilterReply.ACCEPT
import static ch.qos.logback.core.spi.FilterReply.DENY

import java.nio.charset.Charset

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.LevelFilter
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

appender("CONSOLE", ConsoleAppender) {
    filter(LevelFilter) {
        level = INFO
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%date [%thread] %-5level [%logger{50}] %file:%line - %msg%n"
        charset = Charset.forName("UTF-8")
    }
}
appender("FILE_INFO", RollingFileAppender) {
    filter(LevelFilter) {
        level = ERROR
        onMatch = DENY
        onMismatch = ACCEPT
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "logs/spring-boot-starter/info.created_on_%d{yyyy-MM-dd}.part_%i.log"
        maxHistory = 90
        timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) {
            maxFileSize = "20MB"
        }
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%date [%thread] %-5level [%logger{50}] %file:%line - %msg%n"
        charset = Charset.forName("UTF-8")
    }
}
appender("FILE_ERROR", RollingFileAppender) {
    filter(ThresholdFilter) {
        level = ERROR
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "logs/spring-boot-starter/error.created_on_%d{yyyy-MM-dd}.part_%i.log"
        maxHistory = 90
        timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) {
            maxFileSize = "20MB"
        }
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%date [%thread] %-5level [%logger{50}] %file:%line - %msg%n"
        charset = Charset.forName("UTF-8")
    }
}
root(INFO, [
    "CONSOLE",
    "FILE_INFO",
    "FILE_ERROR"
])