package bowling;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PartieMultiJoueursTest {

	@Test
	void testDemarreNouvellePartie() {
		PartieMultiJoueurs partie = new PartieMultiJoueurs();
		String resultat = partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});

		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 1", resultat);
	}

	@Test
	void testDemarreNouvellePartieSansJoueurs() {
		PartieMultiJoueurs partie = new PartieMultiJoueurs();
		assertThrows(IllegalArgumentException.class, () -> partie.demarreNouvellePartie(new String[]{}));
	}

	@Test
	void testEnregistreLancerAvecUnJoueur() {
		PartieMultiJoueurs partie = new PartieMultiJoueurs();
		partie.demarreNouvellePartie(new String[]{"Pierre"});

		String resultat1 = partie.enregistreLancer(5);
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2", resultat1);

		String resultat2 = partie.enregistreLancer(4);
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", resultat2);

		int scorePierre = partie.scorePour("Pierre");
		assertEquals(9, scorePierre);
	}

	@Test
	void testEnregistreLancerAvecStrike() {
		PartieMultiJoueurs partie = new PartieMultiJoueurs();
		partie.demarreNouvellePartie(new String[]{"Pierre"});

		String resultat1 = partie.enregistreLancer(10);
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", resultat1);

		String resultat2 = partie.enregistreLancer(7);
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 2", resultat2);

		String resultat3 = partie.enregistreLancer(2);
		assertEquals("Prochain tir : joueur Pierre, tour n° 3, boule n° 1", resultat3);

		int scorePierre = partie.scorePour("Pierre");
		assertEquals(28, scorePierre); // Strike bonus + suivant
	}

	@Test
	void testEnregistreLancerAvecSpare() {
		PartieMultiJoueurs partie = new PartieMultiJoueurs();
		partie.demarreNouvellePartie(new String[]{"Pierre"});

		String resultat1 = partie.enregistreLancer(6);
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2", resultat1);

		String resultat2 = partie.enregistreLancer(4); // Spare
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", resultat2);

		String resultat3 = partie.enregistreLancer(5);
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 2", resultat3);

		int scorePierre = partie.scorePour("Pierre");
		assertEquals(20, scorePierre); // Spare bonus
	}


	@Test
	void testPartieTerminee() {
		PartieMultiJoueurs partie = new PartieMultiJoueurs();
		partie.demarreNouvellePartie(new String[]{"Pierre"});

		// Effectuer 20 lancers dans la rigole (score = 0)
		for (int i = 0; i < 20; i++) {
			partie.enregistreLancer(0);
		}

		// Vérifier que la partie est terminée
		assertThrows(IllegalStateException.class, () -> partie.enregistreLancer(0));

		// Vérifier le score final
		int scorePierre = partie.scorePour("Pierre");
		assertEquals(0, scorePierre);
	}


	@Test
	void testScorePourJoueurInconnu() {
		PartieMultiJoueurs partie = new PartieMultiJoueurs();
		partie.demarreNouvellePartie(new String[]{"Pierre"});

		assertThrows(IllegalArgumentException.class, () -> partie.scorePour("Jacques"));
	}

	@Test
	void testPartiePerfect() {
		PartieMultiJoueurs partie = new PartieMultiJoueurs();
		partie.demarreNouvellePartie(new String[]{"Pierre"});

		for (int i = 0; i < 12; i++) {
			partie.enregistreLancer(10); // Perfect game
		}

		int scorePierre = partie.scorePour("Pierre");
		assertEquals(300, scorePierre); // Score maximum
	}

	@Test
	void testExempleDeDeroulement() {
		PartieMultiJoueurs partie = new PartieMultiJoueurs();

		// Démarrage de la partie avec deux joueurs
		String debut = partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 1", debut);

		// Pierre lance sa première boule
		String tir1 = partie.enregistreLancer(5);
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2", tir1);

		// Pierre lance sa deuxième boule
		String tir2 = partie.enregistreLancer(3);
		assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 1", tir2);

		// Paul fait un strike
		String tir3 = partie.enregistreLancer(10);
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", tir3);

		// Pierre lance sa première boule au deuxième tour
		String tir4 = partie.enregistreLancer(7);
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 2", tir4);

		// Pierre lance sa deuxième boule au deuxième tour
		String tir5 = partie.enregistreLancer(3);
		assertEquals("Prochain tir : joueur Paul, tour n° 2, boule n° 1", tir5);

		// Scores intermédiaires
		int scorePierre = partie.scorePour("Pierre");
		assertEquals(18, scorePierre); // 5+3 + (7+3)

		int scorePaul = partie.scorePour("Paul");
		assertEquals(10, scorePaul); // Strike, mais pas encore de bonus ajouté (manque deux lancers suivants)

		// Vérifier exception pour joueur inconnu
		assertThrows(IllegalArgumentException.class, () -> partie.scorePour("Jacques"));
	}

}
