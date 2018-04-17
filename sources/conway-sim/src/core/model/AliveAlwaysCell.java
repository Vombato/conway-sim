package core.model;

/**
 * Implementation of {@link Cell} which is always alive and can't be murdered.
 */
public class AliveAlwaysCell implements Cell {

    /**
     * This is the code returned by {@link SimpleCell#code}.
     */
    public static final int ALIVE_ALWAYS_CODE = 3;

    private Status currentStatus = Status.ALIVE;

    /**
     * Constructor method for a new alive Cell. No parameters needed as it won't
     * change.
     */
    public AliveAlwaysCell() {
    }

    /**
     * Is the method to change the status of this cell. Won't work as this change is
     * not supported.
     * 
     * @param nextStatus
     */
    @Override
    public void setStatus(final Status nextStatus) {
    }

    /**
     * Is the method which gives the current Status of this cell.
     */
    @Override
    public Status getStatus() {
        return this.currentStatus;
    }

    /**
     * Is the method which gives an exact copy of this cell.
     */
    @Override
    public Cell copy() {
        return new AliveAlwaysCell();
    }

    /**
     * Is the method which gives the code of this kind of cells.
     */
    @Override
    public int code() {
        return AliveAlwaysCell.ALIVE_ALWAYS_CODE;
    }

}
