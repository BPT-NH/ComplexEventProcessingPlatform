package sushi.transformation.element;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Range element for filter expressions.
 */
@Entity
@Table(name = "RangeElement")
public class RangeElement implements Serializable {
	
	private static final long serialVersionUID = 8502951013262132211L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RangeElement_ID")
	private int ID;
	
	@Column(name = "LeftEndpoint")
	private int leftEndpoint;
	
	@Column(name = "LeftEndpointOpen")
	private boolean leftEndpointOpen;
	
	@Column(name = "RightEndpoint")
	private int rightEndpoint;
	
	@Column(name = "RightEndpointOpen")
	private boolean rightEndpointOpen;
	
	public RangeElement() {
		this.ID = 0;
		this.leftEndpoint = 0;
		this.leftEndpointOpen = false;
		this.rightEndpoint = 1;
		this.rightEndpointOpen = false;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param leftEndpoint left endpoint of a range
	 * @param leftEndpointOpen true if left endpoint is open
	 * @param rightEndpoint right endpoint of a range
	 * @param rightEndpointOpen true if right endpoint is open
	 */
	public RangeElement(int leftEndpoint, boolean leftEndpointOpen, int rightEndpoint, boolean rightEndpointOpen) {
		this();
		this.leftEndpoint = leftEndpoint;
		this.leftEndpointOpen = leftEndpointOpen;
		this.rightEndpoint = rightEndpoint;
		this.rightEndpointOpen = rightEndpointOpen;
	}
	
	public int getLeftEndpoint() {
		return leftEndpoint;
	}

	public void setLeftEndpoint(int leftEndpoint) {
		this.leftEndpoint = leftEndpoint;
	}

	public boolean isLeftEndpointOpen() {
		return leftEndpointOpen;
	}

	public void setLeftEndpointOpen(boolean leftEndpointOpen) {
		this.leftEndpointOpen = leftEndpointOpen;
	}

	public int getRightEndpoint() {
		return rightEndpoint;
	}

	public void setRightEndpoint(int rightEndpoint) {
		this.rightEndpoint = rightEndpoint;
	}

	public boolean isRightEndpointOpen() {
		return rightEndpointOpen;
	}

	public void setRightEndpointOpen(boolean rightEndpointOpen) {
		this.rightEndpointOpen = rightEndpointOpen;
	}
}
