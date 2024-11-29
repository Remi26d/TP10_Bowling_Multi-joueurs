package bowling;

import java.util.HashMap;
import java.util.Map;

public class PartieMultiJoueurs implements IPartieMultiJoueurs {
	private Map<String, PartieMonoJoueur> joueurs;
	private String[] ordreJoueurs;
	private int indexJoueurCourant;
	private boolean partieDemarree;

	public PartieMultiJoueurs() {
		this.joueurs = new HashMap<>();
		this.partieDemarree = false;
	}

	@Override
	public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException {
		if (nomsDesJoueurs == null || nomsDesJoueurs.length == 0) {
			throw new IllegalArgumentException("Il faut au moins un joueur.");
		}

		for (String nom : nomsDesJoueurs) {
			joueurs.put(nom, new PartieMonoJoueur());
		}

		this.ordreJoueurs = nomsDesJoueurs;
		this.indexJoueurCourant = 0;
		this.partieDemarree = true;

		return prochainTir();
	}

	@Override
	public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {
		if (!partieDemarree) {
			throw new IllegalStateException("La partie n'est pas démarrée.");
		}

		String joueurCourant = ordreJoueurs[indexJoueurCourant];
		PartieMonoJoueur partieJoueur = joueurs.get(joueurCourant);

		partieJoueur.enregistreLancer(nombreDeQuillesAbattues);

		// Passer au prochain joueur ou rester sur le même si le tour n'est pas fini
		if (partieJoueur.numeroProchainLancer() == 1) {
			indexJoueurCourant = (indexJoueurCourant + 1) % ordreJoueurs.length;
		}

		// Vérifier si tous les joueurs ont fini
		if (tousLesJoueursOntTermine()) {
			partieDemarree = false;
			return "Partie terminée";
		}

		return prochainTir();
	}

	@Override
	public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
		PartieMonoJoueur partieJoueur = joueurs.get(nomDuJoueur);
		if (partieJoueur == null) {
			throw new IllegalArgumentException("Joueur inconnu");
		}
		return partieJoueur.score();
	}


	private String prochainTir() {
		String joueurCourant = ordreJoueurs[indexJoueurCourant];
		PartieMonoJoueur partieJoueur = joueurs.get(joueurCourant);

		return String.format(
			"Prochain tir : joueur %s, tour n° %d, boule n° %d",
			joueurCourant,
			partieJoueur.numeroTourCourant(),
			partieJoueur.numeroProchainLancer()
		);
	}

	private boolean tousLesJoueursOntTermine() {
		for (String joueur : ordreJoueurs) {
			if (!joueurs.get(joueur).estTerminee()) {
				return false;
			}
		}
		return true;
	}
}
