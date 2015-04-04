package ligueBaseball;

/**
 * L'exception BiblioException est lev�e lorsqu'une transaction est inad�quate.
 * Par exemple
 *   -- livre inexistant
 */

public final class LigueException extends Exception
{
  public LigueException(String message)
  {
    super (message);
  }
}
