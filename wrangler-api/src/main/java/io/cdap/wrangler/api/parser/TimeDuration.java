/*
 * Copyright © 2025 Hitaishi Singhaniya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cdap.wrangler.api.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class TimeDuration implements Token {
    private static final Pattern PATTERN = Pattern.compile("(?i)(\\d+(\\.\\d+)?)(ms|s|m|h)");
    private final long milliseconds;

    public TimeDuration(String value) {
        Matcher matcher = PATTERN.matcher(value.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid time duration: " + value);
        }

        double number = Double.parseDouble(matcher.group(1));
        String unit = matcher.group(3).toLowerCase();

        switch (unit) {
            case "ms": this.milliseconds = (long) number; break;
            case "s": this.milliseconds = (long) (number * 1000); break;
            case "m": this.milliseconds = (long) (number * 60 * 1000); break;
            case "h": this.milliseconds = (long) (number * 60 * 60 * 1000); break;
            default: throw new IllegalArgumentException("Unknown time unit: " + unit);
        }
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    @Override
    public TokenType type() {
        return TokenType.TIME_DURATION;
    }

    @Override
    public Object value() {
        return milliseconds;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(milliseconds);
    }

    @Override
    public String toString() {
        return milliseconds + " ms";
    }
}
