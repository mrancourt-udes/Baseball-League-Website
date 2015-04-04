package ligueBaseball;

/**
 * Classe EnregistrementInvalideException.
 * @author 	Martin Rancourt
 * @author 	Vincent Ribou
 * @version 1.0
 * @since   2015-02-11
 */
public class EnregistrementInexistentException extends Exception {

	/**
	 * Détermine si un fichier de serialisation est compatible avec cette classe.
	 */
	private static final long serialVersionUID = -8500729360161449115L;

	/**
	 * Exception d'enregistrement invalide.
	 * @param message Description de l'exception.
	 */
	public EnregistrementInexistentException(String message) 
	{
		super(message);
	}
}