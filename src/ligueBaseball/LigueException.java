package ligueBaseball;

/**
 * L'exception LigueException est levee lorsqu'une transaction est inadequate.
 * Par exemple
 *   -- Joueur inexistant
 */

public final class LigueException extends Exception
{
  public LigueException(String message)
  {
    super (message);
  }
}
