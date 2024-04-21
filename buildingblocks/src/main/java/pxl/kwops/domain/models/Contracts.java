package pxl.kwops.domain.models;

import pxl.kwops.domain.exceptions.ContractException;

public class Contracts
{
    public static void require(boolean precondition, String message)
    {
        if (!precondition)
        {
            throw new ContractException(message);
        }
    }
}
