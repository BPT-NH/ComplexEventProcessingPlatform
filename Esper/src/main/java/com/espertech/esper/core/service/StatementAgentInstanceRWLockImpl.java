/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.service;

import com.espertech.esper.util.ThreadLogUtil;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Simple read-write lock based on {@link java.util.concurrent.locks.ReentrantReadWriteLock} that associates a
 * name with the lock and traces read/write locking and unlocking.
 */
public class StatementAgentInstanceRWLockImpl implements StatementAgentInstanceLock
{
    private final ReentrantReadWriteLock lock;

    /**
     * Ctor.
     * @param isFair true if a fair lock, false if not
     */
    public StatementAgentInstanceRWLockImpl(boolean isFair)
    {
        lock = new ReentrantReadWriteLock(isFair);
    }

    /**
     * Lock write lock.
     */
    public void acquireWriteLock(StatementLockFactory statementLockFactory)
    {
        if (ThreadLogUtil.ENABLED_TRACE)
        {
            ThreadLogUtil.traceLock(ACQUIRE_TEXT + " write ", lock);
        }
        lock.writeLock().lock();
        if (ThreadLogUtil.ENABLED_TRACE)
        {
            ThreadLogUtil.traceLock(ACQUIRED_TEXT + " write ", lock);
        }
    }

    /**
     * Unlock write lock.
     */
    public void releaseWriteLock(StatementLockFactory statementLockFactory)
    {
        if (ThreadLogUtil.ENABLED_TRACE)
        {
            ThreadLogUtil.traceLock(RELEASE_TEXT + " write ", lock);
        }
        lock.writeLock().unlock();
        if (ThreadLogUtil.ENABLED_TRACE)
        {
            ThreadLogUtil.traceLock(RELEASED_TEXT + " write ", lock);
        }
    }

    /**
     * Lock read lock.
     */
    public void acquireReadLock()
    {
        if (ThreadLogUtil.ENABLED_TRACE)
        {
            ThreadLogUtil.traceLock(ACQUIRE_TEXT + " read ", lock);
        }
        lock.readLock().lock();
        if (ThreadLogUtil.ENABLED_TRACE)
        {
            ThreadLogUtil.traceLock(ACQUIRED_TEXT + " read ", lock);
        }
    }

    /**
     * Unlock read lock.
     */
    public void releaseReadLock()
    {
        if (ThreadLogUtil.ENABLED_TRACE)
        {
            ThreadLogUtil.traceLock(RELEASE_TEXT + " read ", lock);
        }
        lock.readLock().unlock();
        if (ThreadLogUtil.ENABLED_TRACE)
        {
            ThreadLogUtil.traceLock(RELEASED_TEXT + " read ", lock);
        }
    }

    public String toString()
    {
        return this.getClass().getSimpleName();
    }

}
