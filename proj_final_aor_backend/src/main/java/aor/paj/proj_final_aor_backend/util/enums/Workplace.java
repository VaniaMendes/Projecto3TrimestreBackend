package aor.paj.proj_final_aor_backend.util.enums;

public enum Workplace {
    LISBOA, COIMBRA, PORTO, TOMAR, VISEU, VILA_REAL;

    public static String getWorkplaceNameByOrdinal(int ordinal) {
        if (ordinal >= 0 && ordinal < Workplace.values().length) {
            return Workplace.values()[ordinal].name();
        } else {
            throw new IllegalArgumentException("Invalid ordinal for Workplace enum.");
        }
    }

}
