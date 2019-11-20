/**
 * $Id$
 * @author vmateu
 * @date   Sep 29, 2015 11:28:43 AM
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package cat.udl.cig.fields.groups;

import cat.udl.cig.fields.elements.GroupElement;

/**
 *
 */
public interface MultiplicativeSubgroup extends Group {

    public GroupElement getGenerator();
}
