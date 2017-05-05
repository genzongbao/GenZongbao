package ydh.cicada.api;

import java.lang.annotation.*;

/**
 * seterAndGeter annotation
 * @author tearslee
 *
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SetAndGet
{
  public abstract boolean set();

  public abstract boolean get();
}