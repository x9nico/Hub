package net.samagames.hub.utils;

import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.Via;

public class RestrictedVersion
{
    public enum Operator
    {
        EQUALS("="),
        GREATER_OR_EQUALS(">="),
        LESS_OR_EQUALS("<=");

        private String symbol;

        Operator(String symbol)
        {
            this.symbol = symbol;
        }

        public String getSymbol()
        {
            return this.symbol;
        }

        public static Operator getBySymbol(String symbol)
        {
            for (Operator operator : values())
                if (operator.getSymbol().equals(symbol))
                    return operator;

            return null;
        }
    }

    private final Operator operator;
    private final int protocolVersion;
    private final String literalVersion;

    private RestrictedVersion(Operator operator, int protocolVersion, String literalVersion)
    {
        this.operator = operator;
        this.protocolVersion = protocolVersion;
        this.literalVersion = literalVersion;
    }

    public Operator getOperator()
    {
        return this.operator;
    }

    public int getProtocolVersion()
    {
        return this.protocolVersion;
    }

    public String getLiteralVersion()
    {
        return this.literalVersion;
    }

    public boolean canAccess(int askingProtocolVersion)
    {
        if (this.operator == Operator.EQUALS)
            return askingProtocolVersion == this.protocolVersion;
        else if (this.operator == Operator.GREATER_OR_EQUALS)
            return askingProtocolVersion >= this.protocolVersion;
        else if (this.operator == Operator.LESS_OR_EQUALS)
            return askingProtocolVersion <= this.protocolVersion;

        return false;
    }

    public boolean canAccess(Player player)
    {
        return this.canAccess(Via.getAPI().getPlayerVersion(player));
    }

    public static RestrictedVersion parse(String versionLine) throws Exception
    {
        if (versionLine == null)
            return null;

        int openingParenthesesAt = versionLine.indexOf('(');
        int closingParenthesesAt = versionLine.indexOf(')');

        if (openingParenthesesAt == -1 || closingParenthesesAt == -1)
        {
            throw new Exception("No literal version found");
        }

        String operatorSymbol = String.valueOf(versionLine.charAt(0));

        int beginSubstringIndex = 1;

        if (versionLine.charAt(1) == '=')
        {
            operatorSymbol += "=";
            beginSubstringIndex = 2;
        }

        Operator operator = Operator.getBySymbol(operatorSymbol);

        if (operator == null)
        {
            throw new Exception("Operator symbol isn't valid! (" + operatorSymbol + ")");
        }

        int protocolVersion = Integer.parseInt(versionLine.substring(beginSubstringIndex, openingParenthesesAt));
        String literalVersion = versionLine.substring(openingParenthesesAt + 1, closingParenthesesAt);

        return new RestrictedVersion(operator, protocolVersion, literalVersion);
    }

    public static boolean isLoggedInPost19(Player player)
    {
        return Via.getAPI().getPlayerVersion(player) >= 107;
    }
}
