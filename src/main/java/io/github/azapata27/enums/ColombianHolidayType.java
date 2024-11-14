package io.github.azapata27.enums;

/**
 * Official Colombian holiday types according to Law 51 of 1983.
 * Holidays are classified by their nature (CIVIL or RELIGIOUS) and their calculation rules.
 * <p>
 * The classification follows the Colombian legislation which establishes:
 * <ul>
 *   <li>{@link #FIXED_CIVIL} - Civil holidays with fixed dates that don't change
 *       (e.g., Año Nuevo - Jan 1, Día del Trabajo - May 1, Independencia Nacional - Jul 20)</li>
 *
 *   <li>{@link #FIXED_RELIGIOUS} - Religious holidays with fixed dates
 *       (e.g., Navidad - Dec 25, Inmaculada Concepción - Dec 8)</li>
 *
 *   <li>{@link #EASTER_BASED_RELIGIOUS} - Religious holidays calculated from Easter Sunday that remain on their date
 *       (e.g., Jueves Santo [-3 days], Viernes Santo [-2 days])</li>
 *
 *   <li>{@link #TRANSFERABLE_CIVIL} - Civil holidays that move to the following Monday
 *       (e.g., Día de la Diversidad Étnica y Cultural, Independencia de Cartagena)</li>
 *
 *   <li>{@link #TRANSFERABLE_RELIGIOUS} - Religious holidays that always move to Monday
 *       (e.g., Reyes Magos, San José, Asunción de la Virgen)</li>
 * </ul>
 *
 * <p><b>Legal References:</b></p>
 * <ul>
 *   <li><a href="https://www.funcionpublica.gov.co/eva/gestornormativo/norma.php?i=4954">Law 51 of 1983</a></li>
 *   <li><a href="https://es.wikipedia.org/wiki/Anexo:D%C3%ADas_festivos_en_Colombia">Colombian Holidays Reference</a></li>
 * </ul>
 *
 * @see HolidayType Base holiday type classification
 */
public enum ColombianHolidayType {

    /**
     * Civil holidays with fixed dates that don't change.
     * Examples: Año Nuevo (Jan 1), Día del Trabajo (May 1)
     */
    FIXED_CIVIL(HolidayType.FIXED, "Fixed Civil Holiday", true),

    /**
     * Religious holidays with fixed dates.
     * Examples: Navidad (Dec 25), Inmaculada Concepción (Dec 8)
     */
    FIXED_RELIGIOUS(HolidayType.FIXED, "Fixed Religious Holiday", false),

    /**
     * Religious holidays calculated relative to Easter Sunday.
     * Examples: Jueves Santo (-3 days), Viernes Santo (-2 days)
     */
    EASTER_BASED_RELIGIOUS(HolidayType.RELATIVE_TO_DATE, "Easter Based Religious Holiday", false),

    /**
     * Civil holidays that move to the following Monday.
     * Examples: Día de la Diversidad Étnica y Cultural, Independencia de Cartagena
     */
    TRANSFERABLE_CIVIL(HolidayType.TRANSFERABLE, "Transferable Civil Holiday", true),

    /**
     * Religious holidays that move to the following Monday.
     * Examples: Epifanía, San José, Asunción de la Virgen
     */
    TRANSFERABLE_RELIGIOUS(HolidayType.TRANSFERABLE, "Easter Based Transferable Religious Holiday", false);

    private final HolidayType type;
    private final String description;
    private final boolean isCivil;

    /**
     * Constructs a Colombian holiday type.
     *
     * @param type the base holiday type
     * @param description human-readable description of the holiday type
     * @param isCivil true if it's a civil holiday, false if religious
     */
    ColombianHolidayType(HolidayType type, String description, boolean isCivil) {
        this.type = type;
        this.description = description;
        this.isCivil = isCivil;
    }

    /**
     * Gets the base holiday type.
     *
     * @return the base {@link HolidayType}
     */
    public HolidayType getType() {
        return type;
    }

    /**
     * Gets the human-readable description.
     *
     * @return the description of this holiday type
     */
    public String getDescription() {
        return description;
    }

    /**
     * Indicates if this is a civil holiday.
     *
     * @return true if civil, false if religious
     */
    public boolean isCivil() {
        return isCivil;
    }

}