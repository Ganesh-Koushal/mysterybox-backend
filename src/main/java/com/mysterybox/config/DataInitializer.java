package com.mysterybox.config;

import com.mysterybox.entity.*;
import com.mysterybox.repository.MysteryCaseRepository;
import com.mysterybox.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final MysteryCaseRepository caseRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.default-email:admin@mysterybox.com}")
    private String defaultAdminEmail;

    @Value("${app.admin.default-password:Admin@123}")
    private String defaultAdminPassword;

    public DataInitializer(UserRepository userRepository,
                           MysteryCaseRepository caseRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.caseRepository = caseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedAdminUser();
        seedDemoInvestigator();
        seedSampleCases();
    }

    private void seedDemoInvestigator() {
        String demoEmail = "holmes@test.com";
        if (!userRepository.existsByEmail(demoEmail)) {
            User investigator = new User(
                    "Detective Holmes",
                    demoEmail,
                    passwordEncoder.encode("Secret@123"),
                    Role.USER
            );
            userRepository.save(investigator);
            logger.info("Demo investigator created with email: {}", demoEmail);
        }
    }

    private void seedAdminUser() {
        if (!userRepository.existsByEmail(defaultAdminEmail.toLowerCase())) {
            User admin = new User(
                    "Chief Inspector",
                    defaultAdminEmail.toLowerCase(),
                    passwordEncoder.encode(defaultAdminPassword),
                    Role.ADMIN
            );
            userRepository.save(admin);
            logger.info("Default administrator created with email: {}", defaultAdminEmail);
        }
    }

    private void seedSampleCases() {
        if (caseRepository.count() == 0) {
            // Case 1
            MysteryCase case1 = new MysteryCase(
                    "The Midnight Diamond of Thornwood Hall",
                    "During a violent midnight thunderstorm at Thornwood Hall, Lady Eleanor's celebrated 50-carat blue diamond vanished from her locked display case. The study door was bolted from the inside, yet muddy size 8 footprints and the faint aroma of rare almond tobacco were found leading to the terrace window.",
                    Difficulty.EASY,
                    true
            );

            case1.addClue(new Clue(case1, "The Locked Study Door", "The study door remained bolted from within, but the terrace window latch was quietly unscrewed from the inside earlier in the afternoon.", 1));
            case1.addClue(new Clue(case1, "The Smoker's Signature", "A dropped monogrammed silver case held Turkish tobacco blended with bitter almond essence—an imported blend known to be smoked by only one guest.", 2));
            case1.addClue(new Clue(case1, "Footprints on the Lawn", "Muddy footprints from size 8 Oxford shoes led across the rose garden toward the carriage house.", 3));

            case1.addEvidence(new Evidence(case1, "Plaster Cast of Footprint", "Footprint impression matching size 8 shoes with an uneven worn left heel. Butler Arthur wears size 10; nephew Charles wears size 8."));
            case1.addEvidence(new Evidence(case1, "Financial Ledger", "A gambling debt notice amounting to £50,000 due this week was found addressed to Charles Thornwood."));
            case1.addEvidence(new Evidence(case1, "Almond Tobacco Pouch", "Found soaked in rain near the terrace steps, confirming the thief stood outside waiting for Lady Eleanor to fall asleep."));

            case1.setSolution(new Solution(case1, "Charles Thornwood"));
            caseRepository.save(case1);

            // Case 2
            MysteryCase case2 = new MysteryCase(
                    "The Alchemist's Cyanide Cipher",
                    "Eminent university biochemist Dr. Julian Reed was discovered dead in his laboratory over a spilled flask of potassium cyanide. While initial signs suggested a tragic accident, a coded cipher left in his lab journal and an unauthorized keycard swipe indicate premeditated murder.",
                    Difficulty.MEDIUM,
                    true
            );

            case2.addClue(new Clue(case2, "The Spilled Espresso", "Toxicology reveals concentrated chloral hydrate sedative in Dr. Reed's half-finished espresso, taken 30 minutes before the cyanide exposure.", 1));
            case2.addClue(new Clue(case2, "The Electronic Log", "Keycard #104 registered to Lab Director Professor Harrison was swiped at the security gate at 11:42 PM, contradicting his alibi of being in London.", 2));
            case2.addClue(new Clue(case2, "The Missing Formula", "The vault containing the patent application for the synthetic catalyst was emptied, and the patent file was wiped from the hard drive.", 3));

            case2.addEvidence(new Evidence(case2, "Decrypted Diary Fragment", "Dr. Reed's notes state: 'Harrison demanded half the patent royalties today. Threatened to ensure I never publish.'"));
            case2.addEvidence(new Evidence(case2, "Train Ticket Stamped", "The train ticket produced by Professor Harrison was stamped at Victoria Station at 1:15 AM, proving he departed long after the midnight murder."));
            case2.addEvidence(new Evidence(case2, "Latex Glove Residue", "Powdered cyanide traces were recovered from the waste chute outside Professor Harrison's personal office."));

            case2.setSolution(new Solution(case2, "Professor Harrison"));
            caseRepository.save(case2);

            // Case 3
            MysteryCase case3 = new MysteryCase(
                    "The Phantom of the Cyber Vault",
                    "During a midnight quarterly security audit at Obsidian Financial, 40 million encrypted bearer tokens vanished from an air-gapped cryptographic vault. The breach bypassed multi-factor biometric authentication using custom forensic bypass software created by one of the internal executives.",
                    Difficulty.HARD,
                    true
            );

            case3.addClue(new Clue(case3, "Zero-Day Exploit Origin", "The intrusion script executed through terminal DX-9 was built using a development compiler only installed on Chief Information Officer Elena Rostova's system.", 1));
            case3.addClue(new Clue(case3, "Spoofed Biometrics", "A high-precision silicone facsimile of Chief Security Officer Marcus Vance's thumbprint was used to unlock the primary physical vault door.", 2));
            case3.addClue(new Clue(case3, "Camera Static Glitch", "The server room CCTV recorded four minutes of synthetic loop video triggered from the CIO administration console.", 3));

            case3.addEvidence(new Evidence(case3, "Latex Mould Fragment", "A silicone thumbprint mould with trace cosmetic powder matching CIO Elena Rostova was recovered in the executive washroom."));
            case3.addEvidence(new Evidence(case3, "Offshore Ledger Routing", "The blockchain tracer pinpointed micro-transfers redirecting to a private Cayman entity linked to Elena Rostova."));
            case3.addEvidence(new Evidence(case3, "One-Way Flight Ticket", "A chartered private flight to Zurich departing at 05:00 AM booked under Elena Rostova's alias was discovered on her secondary smartphone."));

            case3.setSolution(new Solution(case3, "Elena Rostova"));
            caseRepository.save(case3);

            logger.info("Successfully seeded 3 initial mystery cases with clues, evidence, and solutions.");
        }
    }
}
