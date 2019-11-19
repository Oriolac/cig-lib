/**
 * $Id$
 * @author vmateu
 * @date   Jul 6, 2015 12:19:30 PM
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package cat.udl.cig.cryptography.cryptosystems;

/**
 *
 */
public interface HomomorphicCryptosystem extends Cryptosystem {

    /**
     * Returns the <i>HomomorphicCypher</i> {@code chyper} of the
     * <i>Cryptosystem</i>.
     *
     * @return the <i>HomomorphicCypher</i> {@code chyper} of {@code this}
     *         <i>Cryptosystem</i>.
     * @see HomomorphicCypher
     */
    public HomomorphicCypher getCypher();
}
