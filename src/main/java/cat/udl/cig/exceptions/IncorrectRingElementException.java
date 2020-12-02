/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.exceptions;

/**
 * @author M.Àngels Cerveró
 */
public class IncorrectRingElementException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public IncorrectRingElementException() {
        super();
    }

    public IncorrectRingElementException(final String message) {
        super(message);
    }
}
