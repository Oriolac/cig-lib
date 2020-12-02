/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.exceptions;

/**
 * @author M.Àngels Cerveró
 */
public class NotImplementedException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NotImplementedException() {
        super();
    }

    public NotImplementedException(final String message) {
        super(message);
    }
}
