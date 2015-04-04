package ligueBaseball;

/**
 * L'exception BiblioException est lev�e lorsqu'une transaction est inad�quate.
 * Par exemple
 *   -- livre inexistant
 */

public final class LigueBaseballException extends Exception
{
  public LigueBaseballException (String message)
  {
  super (message);
  }
}
