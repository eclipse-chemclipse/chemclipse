/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"num", "description", "len", "hsps"})
@XmlRootElement(name = "Hit")
public class Hit {

	@XmlElement(required = true)
	protected BigInteger num;

	@XmlElement(required = true)
	protected Hit.Description description;

	@XmlElement(required = true)
	protected BigInteger len;

	protected Hit.Hsps hsps;

	public BigInteger getNum() {

		return num;
	}

	public void setNum(BigInteger value) {

		this.num = value;
	}

	public Hit.Description getDescription() {

		return description;
	}

	public void setDescription(Hit.Description value) {

		this.description = value;
	}

	public BigInteger getLen() {

		return len;
	}

	public void setLen(BigInteger value) {

		this.len = value;
	}

	public Hit.Hsps getHsps() {

		return hsps;
	}

	public void setHsps(Hit.Hsps value) {

		this.hsps = value;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"hitDescr"})
	public static class Description {

		@XmlElement(name = "HitDescr")
		protected List<HitDescr> hitDescr;

		public List<HitDescr> getHitDescr() {

			if(hitDescr == null) {
				hitDescr = new ArrayList<>();
			}
			return this.hitDescr;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"hsp"})
	public static class Hsps {

		@XmlElement(name = "Hsp")
		protected List<Hsp> hsp;

		public List<Hsp> getHsp() {

			if(hsp == null) {
				hsp = new ArrayList<>();
			}
			return this.hsp;
		}
	}
}
