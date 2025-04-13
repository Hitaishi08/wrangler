package io.cdap.wrangler.parser;

import io.cdap.wrangler.api.parser.*;
import io.cdap.wrangler.grammar.DirectiveBaseVisitor;
import io.cdap.wrangler.grammar.DirectiveParser;

public class DirectiveVisitorImpl extends DirectiveBaseVisitor<Token> {

    // Token Group to hold the tokens (Assuming it's defined elsewhere in your code)
    private TokenGroup tokenGroup;

    public DirectiveVisitorImpl(TokenGroup tokenGroup) {
        this.tokenGroup = tokenGroup;
    }

    @Override
    public Token visitByteSizeArg(DirectiveParser.ByteSizeArgContext ctx) {
        String value = ctx.getText();  // Get the text of the byte size argument
        ByteSize byteSizeToken = new ByteSize(value);
        
        // Add the created token to the token group
        tokenGroup.addToken(byteSizeToken);
        
        return byteSizeToken;
    }

    @Override
    public Token visitTimeDurationArg(DirectiveParser.TimeDurationArgContext ctx) {
        String value = ctx.getText();  // Get the text of the time duration argument
        TimeDuration timeDurationToken = new TimeDuration(value);
        
        // Add the created token to the token group
        tokenGroup.addToken(timeDurationToken);
        
        return timeDurationToken;
    }

    @Override
    public Token visitValue(DirectiveParser.ValueContext ctx) {
        // Handle cases where the value is a byte size or time duration argument
        if (ctx.byteSizeArg() != null) {
            return visitByteSizeArg(ctx.byteSizeArg());
        } else if (ctx.timeDurationArg() != null) {
            return visitTimeDurationArg(ctx.timeDurationArg());
        } else if (ctx.STRING() != null) {
            return new Text(ctx.STRING().getText());
        } else if (ctx.NUMBER() != null) {
            return new Number(ctx.NUMBER().getText());
        } else if (ctx.BOOLEAN() != null) {
            return new Bool(ctx.BOOLEAN().getText());
        }

        throw new IllegalArgumentException("Unknown value: " + ctx.getText());
    }
}
